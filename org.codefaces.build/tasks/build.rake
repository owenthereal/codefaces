
namespace :build do
  
#  task :default => [:copy_war_template, :build]
#  
#  PLUGINS_BUILD_DIR = File.join(BUILD_DIR, "plugins")
#  FEATURES_BUILD_DIR = File.join(BUILD_DIR, "features")
#  EXCLUSES_DIR = ["CVS", ".git", ".", ".."]
#  
#  desc "create the build directory"
#  task :prepare do
#      mkdir BUILD_DIR unless File.exists?(BUILD_DIR)
#      mkdir PLUGINS_BUILD_DIR unless File.exists?(PLUGINS_BUILD_DIR)
#      mkdir FEATURES_BUILD_DIR unless File.exists?(FEATURES_BUILD_DIR)
#       
#      Dir.foreach(SRC_DIR) do |f|  
#        if File.directory?(File.join(SRC_DIR, f)) && (!EXCLUSES_DIR.include?(f))
#          if f == CODEFACES_FEATURE
#            cp_r File.join(SRC_DIR, f), FEATURES_BUILD_DIR
#          else
#            cp_r File.join(SRC_DIR, f), PLUGINS_BUILD_DIR
#          end
#        end
#      end
#  end
#  
#  desc "copy the WAR template to the build directory"
#  task :copy_war_template => :prepare do
#    to_dir = File.join(BUILD_DIR, WEBAPP_NAME)
#    mkdir to_dir unless File.exists?(to_dir)
#    copy_entry WAR_TEMPLATE_DIR, to_dir
#  end
#  
#  task :build => :prepare do
#    #sh "java -jar #{CP} -application #{APP} -buildfile #{BUILDFILE} -Dbuilder=#{BUILD_CONF_DIR} generate process assemble package postBuild"
#    sh "java -jar #{CP} -application #{APP} -buildfile #{BUILDFILE} -Dbuilder=#{BUILD_CONF_DIR}"
#  end
  
end