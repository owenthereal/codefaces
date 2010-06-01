require 'tasks/helper'

namespace :package do
  include Helper
  
  desc ""
  task :default => [:prepare, :prepare_war, :build_war]
  
  PACKAGE_DIR = File.expand_path(CONFIGS['environment']['package_dir'])
  SERVLETBRIDGE_BUNDLE = "org.eclipse.equinox.servletbridge*"
  
  # create the package directory
  task :prepare do
    package_dir = PACKAGE_DIR
    create_directory(package_dir)    
  end
  
  desc "Prepare the files to be packed into a war archive"
  task :prepare_war => :prepare do
    war_path = CONFIGS['package']['war_template_path']
    app_name = CONFIGS['app_name']
    cp_r war_path, File.join(PACKAGE_DIR, app_name)
    populate_war
    generate_config_ini
  end
  
  desc "Pack the files into a WAR archive"
  task :build_war  do
    app_name = CONFIGS['app_name']
    current_dir = File.expand_path(".")
    
    Dir.chdir(File.join(PACKAGE_DIR, app_name))
    build_id = Time.now.strftime("%Y%m%d%H%M%S")
    sh "jar -cvf ../#{app_name}_#{build_id}.war *"
    puts "DONE! WAR file can be found in #{File.expand_path(".")}."
    Dir.chdir(current_dir)
  end
  
  
  def populate_war
    build_label = CONFIGS['build']['build_label']
    feature = CONFIGS['build']['codefaces_feature']
    pde_build_dir = CONFIGS['environment']['build_dir']
    build_archive_dir = File.join(pde_build_dir, build_label);
    app_name = CONFIGS['app_name']
    
    #search the build archive in the build directory
    glob_pattern = "#{File.join(build_archive_dir,feature)}*.zip"
    matches = Dir.glob(glob_pattern)

    if matches.size == 0
      raise "No feature build found in #{build_archive_dir}"
    elsif matches.size > 1
      raise "More than 1 builds found in #{build_archive_dir}." 
    end    
   
    # Find the zip file producded by PDE. Unzip it into a temp 
    # folder
    build_archive = matches[0]
    tmpdir = generate_tmp_directory(PACKAGE_DIR)
    sh "unzip #{build_archive} -d #{tmpdir}"
    
    # copy plugin and features
    war_eclipse_dir = File.join(PACKAGE_DIR, app_name, "WEB-INF", "eclipse")
    mv File.join(tmpdir, app_name, "plugins"), war_eclipse_dir
    mv File.join(tmpdir, app_name, "features"), war_eclipse_dir
    
    # put servletbrige to lib
    plugin_dir = File.join(war_eclipse_dir, "plugins")
    Dir.glob(File.join(plugin_dir, SERVLETBRIDGE_BUNDLE)) do |d|
      war_lib_dir = File.join(PACKAGE_DIR, app_name, "WEB-INF", "lib")
      mkdir_p war_lib_dir
      cp d, war_lib_dir
    end
    
    rm_r tmpdir
    puts "built bundles copied. "
  end
  
  def generate_config_ini
    app_name = CONFIGS['app_name']
    plugin_dir = File.join(PACKAGE_DIR, app_name, "WEB-INF/eclipse/plugins")
    s =  "#Eclipse Runtime Configuration File\n"
    s += "osgi.bundles="
    Dir.foreach(plugin_dir) do |d|
      unless ['.','..'].include?(d) || d =~  /^org\.eclipse\.osgi_/
        plugin_name = d.sub(/_\S*/,"")

        cmd = case d
                when /^org\.eclipse\.rap\.rwt\.q07/
                  ""
                when /^org\.eclipse\.equinox\.common/
                  "@2:start"
                else
                  "@start"
              end

        s += "    #{plugin_name}#{cmd},\\\n"
      end
    end
    s += "    org.eclipse.equinox.servletbridge.extensionbundle\n"
    s +=  "osgi.bundles.defaultStartLevel=4\n"
    
    config_path = File.join(PACKAGE_DIR, app_name,
                            "WEB-INF/eclipse/configuration")
    config_file = File.join(config_path, "config.ini")
    puts "config.ini generated."
    File.open(config_file, "w") { |f| f.puts s }
  end
  
  
end
