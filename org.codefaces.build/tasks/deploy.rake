require 'tasks/helper'
require 'net/ssh'
require 'net/sftp'


namespace :deploy do
  include Helper

  # deploy the packaged war file
  task :default => [:deploy]


  # deploy the war file
  task :deploy do
    CONFIGS['deploy']['servers'].each do |server|
      host =  server['server']
      user = server['ssh_user']
      port = server['ssh_port']

      puts "Connecting to #{host}..."
      puts "Enter the password for #{user}"
      passwd = STDIN.gets.chomp.strip
      redo if passwd.empty?

      # find out the path of the compiled war file
      war_file_path = get_war_file()
      file_name = File.basename(war_file_path)
      upload_to = "/home/#{user}/#{file_name}"

      begin
        # upload the file to the server
        upload_war_file(host, port, user, passwd, war_file_path, upload_to)
        # deploy using ssh
        tomcat_home = server['tomcat_home']
        deploy(host, port, user, passwd, tomcat_home, upload_to)
      rescue Exception => e
        puts "Error in making connection"
        puts e
        redo
      end
    end
  end

  # return the path of the compiled war file
  def get_war_file
    app_name = CONFIGS['app_name']
    package_dir = File.expand_path(CONFIGS['environment']['package_dir'])
    warfile_pattern = "#{app_name}_*.war"
    warfile =  Dir.glob( File.join(package_dir, warfile_pattern))[0]

    warfile
  end

  # upload the war file to the server
  def upload_war_file(host, port, user, passwd, war_file_path,
                      remote_path)
    puts "uploading #{war_file_path} to #{host}:#{remote_path}"
    Net::SFTP.start(host, user, :password => passwd, 
                    :port => port) do |sftp|
      sftp.upload!(war_file_path, remote_path);
    end
  end


  # execute a command using sudo in an ssh session
  def sudo_exec(ssh_session, passwd, command)
    cmd = "sudo #{command}"
    puts cmd
    channel = ssh_session.open_channel do |channel|
      channel.request_pty do |ch, success|
        ch.exec "#{cmd}" do |ch, success|

          ch.on_data do |ch, data|
            print data
            STDOUT.flush
            if data =~ /password|Password/
              ch.send_data("#{passwd}\n")
            end
          end

          ch.on_extended_data do |ch, type, data|
            print data
            STDOUT.flush
          end
        end
      end
    end
    channel.wait
  end


  # deploy the war file in remote_warfile_path to tomcat_home/webapps
  # 
  # This method will:
  #   - move the existing war file to tomcat_home/backup
  #   - clean up the cache
  #   - deploy the new war file
  #   - create a symbolic link linking it to ROOT
  #   - restart tomcat
  def deploy(host, port, user, passwd, tomcat_home, remote_warfile_path) 
    # move previous release in tomcat home to backup path
    deploy_to = "#{tomcat_home}/webapps"
    backup_dir = "#{tomcat_home}/backup"

    puts "ssh to #{host}"
    Net::SSH.start(host, user, :password => passwd, 
                         :port => port) do |ssh|

        #backup
        puts "backup existing war files"
        sudo_exec(ssh, passwd, "mkdir #{backup_dir}")
        sudo_exec(ssh, passwd, "mv #{deploy_to}/*.war #{backup_dir}/")

        # clean up the webapp dir
        puts "clean up the webapps directory."
        sudo_exec(ssh, passwd, "rm -rf #{deploy_to}/*")

        # clean up the context xml
        puts "clean up context xml cache"
        context_dir = "#{tomcat_home}/conf/Catalina/localhost"
        sudo_exec(ssh, passwd, "rm -rf #{context_dir}/*.xml")

        # clean up the cache
        puts "clean up Tomcat cache"
        casshe_dir = "#{tomcat_home}/work/Catalina/localhost"
        sudo_exec(ssh, passwd, "rm -rf #{casshe_dir}/*")

        # start tomcat
        puts "make sure tomcat is started"
        sudo_exec(ssh, passwd, "/etc/init.d/tomcat6 start")

        # deploy war file
        puts "deploy the war file"
        deployed_file = "#{deploy_to}/#{File.basename(remote_warfile_path)}"
        sudo_exec(ssh, passwd, "mv #{remote_warfile_path} #{deploy_to}/")

        # create sym link
        puts "create symbolic link"
        new_name = "#{deploy_to}/ROOT"
        deploy_folder = File.basename(deployed_file, ".war")
        sudo_exec(ssh, passwd, "rm #{new_name}")
        sudo_exec(ssh, passwd, "ln -s #{deploy_folder} #{new_name}")

        # restart tomcat
        puts "restart Tomcat"
        sudo_exec(ssh, passwd, "/etc/init.d/tomcat6 restart")
    end
  end

end
