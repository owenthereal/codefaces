require 'tasks/helper'

TARGET_PLATFORM = "rap-runtime*.zip"
SERVLET_BRIDGE_BUNDLES = "org.eclipse.equinox*.servletbridge*.jar"
LAUNCHER = "org.eclipse.equinox.launcher_*.jar"
PDE_BUILD_TOOLS_DIR = "org.eclipse.pde.build_*"
ECLIPSE_PLATFORM_DIR = "eclipse*"
SUBCLIPSE_BUNDLES = "subclipse-*/plugins/*.jar"
SVNKIT_BUNDLES = "org.tmatesoft.svn*.eclipse/plugins/*.jar"

BUILD_DIR = File.expand_path(CONFIGS['environment']['build_dir'])

PDE_BASE_DIR = File.join(BUILD_DIR, "base/eclipse")
PDE_CONFIG_DIR = File.join(BUILD_DIR, "config")
PDE_FEATURES_DIR = File.join(BUILD_DIR, "features")
PDE_PLUGINS_DIR = File.join(BUILD_DIR, "plugins")


namespace :build do
  include Helper
  
  # Perform the whole build
  task :default => [:prepare_pde_build, :pde_build]
  
  desc "Prepare the environment for PDE build"
  task :prepare_pde_build => [:prepare_pde_base_platform,
      :prepare_pde_build_config, :prepare_pde_plugins_and_features]
  
  desc "Run the PDE build"
  task :pde_build do
    tool_dir = File.expand_path(CONFIGS['environment']['tool_dir'])
    eclipse_platform_plugin_dir = Dir.glob(
                                           File.join(tool_dir, ECLIPSE_PLATFORM_DIR, "eclipse/plugins"))[0]
    launcher = Dir.glob(
                        File.join(eclipse_platform_plugin_dir, LAUNCHER))[0]
    pde_build_dir = Dir.glob(
                             File.join(eclipse_platform_plugin_dir, PDE_BUILD_TOOLS_DIR))[0]
    
    app = "org.eclipse.ant.core.antRunner"
    build_file = File.join(pde_build_dir, "scripts", "build.xml")
    build_conf_dir = PDE_CONFIG_DIR
    
    sh "java -jar #{launcher} -application #{app} -buildfile #{build_file} -Dbuilder=#{build_conf_dir} --verbose"
    
    puts "Build can be found in #{File.join(BUILD_DIR, CONFIGS['build']['build_label'])}."
  end
  
  task :prepare => :compress_web_files do
    build_dir = BUILD_DIR
    create_directory(build_dir)
  end
  
  desc "Compress the web files"
  task :compress_web_files do
    src_dir = CONFIGS['environment']['source_dir']
    
    CONFIGS['build']['files_to_compress'].each do |f|
      puts "compressing file: #{f}"
      path = File.join(src_dir, f)
      compress(path)
    end
  end
  
  
  # get the base platform ready 
  task :prepare_pde_base_platform => :prepare do
    build_dir = BUILD_DIR
    tool_dir = File.expand_path(CONFIGS['environment']['tool_dir'])
    
    Dir.glob(File.join(tool_dir, TARGET_PLATFORM)) do |p|
      archive = p
      sh "unzip #{archive} -d #{build_dir}/base/"
    end
    
    Dir.glob(File.join(tool_dir, SERVLET_BRIDGE_BUNDLES)) do |bundle|
      sh "cp #{bundle} #{PDE_BASE_DIR}/plugins"
    end
    
    Dir.glob(File.join(tool_dir, SUBCLIPSE_BUNDLES)) do |bundle|
      sh "cp #{bundle} #{PDE_BASE_DIR}/plugins"
    end
    
    Dir.glob(File.join(tool_dir, SVNKIT_BUNDLES)) do |bundle|
      sh "cp #{bundle} #{PDE_BASE_DIR}/plugins"
    end
  end
  
  task :prepare_pde_build_config => :prepare do
    sh "mkdir -p #{PDE_CONFIG_DIR}" unless File.exists?(PDE_CONFIG_DIR)
    output_path = File.join(PDE_CONFIG_DIR, "build.properties")
    generate_build_config(output_path)
  end
  
  task :prepare_pde_plugins_and_features => :prepare do
    [PDE_PLUGINS_DIR, PDE_FEATURES_DIR].each do |p|
      sh "mkdir -p #{p}" unless File.exists?p
    end
    
    propulate_pde_build_dir
  end
  
  # Generate a PDE build.properties file to the output_path
  def generate_build_config(output_path)
    build_config = CONFIGS['build']
    codefaces_feature = build_config['codefaces_feature'];
    app_name = CONFIGS['app_name']
    build_dir = BUILD_DIR
    base_location =  PDE_BASE_DIR
    build_label = build_config['build_label']
    build_id = Time.now.strftime("%Y%m%d%H%M%S")
    java_src_version = "%1.1f" % build_config['java_src_version']
    java_target_version =  "%1.1f" % build_config['java_target_version']
    
    puts "Generating build.properties"
    template = IO.read(build_config['build_properties_path']) 
    build_properties = ERB.new(template).result(binding)
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
          cp_r path, PDE_FEATURES_DIR
        else
          cp_r path, PDE_PLUGINS_DIR
        end
      end
    end
  end

end
