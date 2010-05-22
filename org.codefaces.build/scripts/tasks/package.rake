require 'tasks/helper'

namespace :package do
  include Helper
  
  task :default => [:prepare, :construct_war_template, :build_war]
  
  PACKAGE_DIR = File.expand_path(CONFIGS['environment']['package_dir'])
  SERVLETBRIDGE_BUNDLE = /^org\.eclipse\.equinox\.servletbridge/
  FRAMEWORK_BUNDLE = /^org\.eclipse\.osgi_/
  
  desc "create the package directory"
  task :prepare do
    
    if(File.exists?(PACKAGE_DIR) && 
       ! Dir.entries(PACKAGE_DIR).select{|x| x unless ['.','..'].include?x}.empty?)
       raise "#{PACKAGE_DIR} is not empty."
    else
      mkdir_p PACKAGE_DIR unless File.exists?(PACKAGE_DIR)
    end
  end
  
  desc "construct the war package"
  task :construct_war_template => :prepare do
    war_path = CONFIGS['package']['war_template_path']
    app_name = CONFIGS['build']['app_name']
    cp_r war_path, File.join(PACKAGE_DIR, app_name)
    populate_war
    generate_config_ini
  end
  
  desc "compress to WAR archive"
  task :build_war => :construct_war_template do
    app_name = CONFIGS['build']['app_name']
    current_dir = File.expand_path(".")
    
    Dir.chdir(File.join(PACKAGE_DIR, app_name))
    sh "jar -cvf ../#{app_name}.war *"
    Dir.chdir(current_dir)
    
    puts "Done!"
  end
  
  
  def populate_war
    build_label = CONFIGS['build']['build_label']
    feature = CONFIGS['build']['codefaces_feature']
    pde_build_dir = File.join(CONFIGS['environment']['build_dir'], 
                              CONFIGS['build']['pde_build_dir'])    
    build_archive_dir = File.join(pde_build_dir, build_label);
    app_name = CONFIGS['build']['app_name']
    
    #search the build archive in the build directory
    glob_pattern = "#{File.join(build_archive_dir,feature)}*.zip"
    matches = Dir.glob(glob_pattern)
    raise "No feature build found in #{build_archive_dir}." if matches.size == 0
    raise "More than 1 builds found in #{build_archive_dir}." if matches.size > 1
    
    build_archive = matches[0]
    tmpdir = generateTmpDirectory(PACKAGE_DIR)
    sh "unzip #{build_archive} -d #{tmpdir}"
    
    # copy plugin and features
    war_eclipse_dir = File.join(PACKAGE_DIR, app_name, "WEB-INF", "eclipse")
    mv File.join(tmpdir, app_name, "plugins"), war_eclipse_dir
    mv File.join(tmpdir, app_name, "features"), war_eclipse_dir
    
    plugin_dir = File.join(war_eclipse_dir, "plugins")
    Dir.foreach(plugin_dir) do |d|
      # put servletbrige to lib
      if d =~ SERVLETBRIDGE_BUNDLE
        war_lib_dir = File.join(PACKAGE_DIR, app_name, "WEB-INF", "lib")
        mkdir_p war_lib_dir
        cp File.join(plugin_dir, d), war_lib_dir
      end  
    end
    
    rm_r tmpdir
    puts "built bundles copied. "
  end
  
  def generate_config_ini
    app_name = CONFIGS['build']['app_name']
    plugin_dir = File.join(PACKAGE_DIR, app_name, "WEB-INF", "eclipse", "plugins")
    s =  "#Eclipse Runtime Configuration File\n"
    s += "osgi.bundles="
    Dir.foreach(plugin_dir) do |d|
      unless ['.','..'].include?(d) || d =~ FRAMEWORK_BUNDLE
        plugin_name = d.sub(/_\S*/,"")
        
        cmd = if plugin_name !~ /^org\.eclipse\.rap\.rwt\.q07/
                if plugin_name =~ /^org\.eclipse\.equinox\.common/ 
                  "@2:start"
                else
                  "@start"
                end
            end
            
        s += "    #{plugin_name}#{cmd},\\\n"
      end 
    end
    s += "    org.eclipse.equinox.servletbridge.extensionbundle\n"
    s +=  "osgi.bundles.defaultStartLevel=4\n"
    
    config_path = File.join(PACKAGE_DIR, app_name, "WEB-INF", "eclipse", "configuration")
    config_file = File.join(config_path, "config.ini")
    puts "config.ini generated."
    File.open(config_file, "w") { |f| f.puts s }
  end
  
  
end