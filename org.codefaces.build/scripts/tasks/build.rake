
namespace :build do
  
  PDE_BUILD_DIR = File.join(CONFIGS['environment']['build_dir'], 
                            CONFIGS['build']['pde_build_dir'])
  PDE_CONFIG_DIR = File.join(CONFIGS['environment']['build_dir'],
                             CONFIGS['build']['pde_build_config_dir'])
                             
  FEATURES_BUILD_DIR = File.join(PDE_BUILD_DIR, "features")
  PLUGINS_BUILD_DIR = File.join(PDE_BUILD_DIR, "plugins")
  
  task :default => [:prepare, :prepare_pde_environment, :build]
  
  desc "create the build directories"
  task :prepare do
    build_dir = File.expand_path(CONFIGS['environment']['build_dir'])
    if(File.exists?(build_dir) && 
       ! Dir.entries(build_dir).select{|x| x unless ['.','..'].include?x}.empty?)
       raise "#{build_dir} is not empty."
    else
      mkdir_p build_dir unless File.exists?(build_dir)
    end
    
    [PDE_BUILD_DIR, PDE_CONFIG_DIR, FEATURES_BUILD_DIR, PLUGINS_BUILD_DIR].each do |d|
      mkdir_p d unless File.exists?d
    end
  end
  
  
  desc "setup the Eclipse PDE building environment"
  task :prepare_pde_environment => :prepare do
    build_properties_path = File.join(PDE_CONFIG_DIR, "build.properties")
    generate_build_config(build_properties_path)
    propulate_pde_build_dir
  end
  
  # Generate a PDE build.properties file to the output_path
  def generate_build_config(output_path)
    build_config = CONFIGS['build']
    build_properties = IO.read(build_config['build_properties_path'])
    
    subs = {
      /\[CODEFACES_FEATURE\]/ => build_config['codefaces_feature'],
      /\[APP_NAME\]/ => build_config['app_name'],
      /\[BUILD_DIR\]/ => File.expand_path(PDE_BUILD_DIR),
      /\[BASE_LOCATION\]/ => File.expand_path(build_config['base_dir']),
      /\[BUILD_LABEL\]/ => build_config['build_label'],
      /\[BUILD_ID\]/ => Time.now.to_i.to_s,
      /\[JAVA_SRC_VERSION\]/ => "%1.1f" % build_config['java_src_version'],
      /\[JAVA_TARGET_VERSION\]/ => "%1.1f" % build_config['java_target_version']
    }
    
    puts "Generating build.properties"
    subs.each_pair do |pattern, sub|
      build_properties.gsub!(pattern, sub)
    end
    
    File.open(output_path, 'w') { |f| f.puts build_properties }
  end
  
  # copy the plugins and features in the source directory to the
  # build directory
  def propulate_pde_build_dir
    src_dir = CONFIGS['environment']['source_dir']
    Dir.foreach(src_dir) do |f|
      path = File.join(src_dir, f)
      if File.directory?(path) && !['.','..'].include?(f)
        if f == CONFIGS['build']['codefaces_feature']
          cp_r path, FEATURES_BUILD_DIR
        else
          cp_r path, PLUGINS_BUILD_DIR
        end
      end
    end
  end

  desc "run the PDE build"
  task :build => :prepare_pde_environment do
    eclipse_home = File.expand_path(CONFIGS['build']['eclipse_home'])
    pde_builde_dir = CONFIGS['build']['pde_dir']
    launcher = CONFIGS['build']['equinox_launcher']
    class_path =  File.join(eclipse_home, "plugins", launcher)
    app = "org.eclipse.ant.core.antRunner"
    build_file = File.join(eclipse_home, "plugins", pde_builde_dir, "scripts", "build.xml")
    build_conf_dir = File.expand_path(PDE_CONFIG_DIR)
    
    sh "java -jar #{class_path} -application #{app} -buildfile #{build_file} -Dbuilder=#{build_conf_dir}"
    
    puts "Build can be found in #{File.join(PDE_BUILD_DIR, CONFIGS['build']['build_label'])}."
  end

end