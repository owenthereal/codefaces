
namespace :fetch do
  
  task :default => [:fetch_git, :fetch_cvs]

  desc "Parpare the directory for fetching"
  task :prepare do
    source_dir = File.expand_path(CONFIGS['environment']['source_dir'])
    if(File.exists?(source_dir) && 
       ! Dir.entries(source_dir).select{|x| x unless ['.','..'].include?x}.empty?)
       raise "#{source_dir} is not empty."
    else
      mkdir_p source_dir unless File.exists?(source_dir)
    end
  end
  
  desc "Fetch the git repositories"
  task :fetch_git => :prepare do
    src_dir = CONFIGS['environment']['source_dir']
    CONFIGS['fetch']['git'].each do |git|
      puts "Fetching: #{git['desciption']}"
      git_repo = git['repository']
      
      tmpdir = generateTmpDirectory(src_dir)
      sh "git clone #{git_repo} #{tmpdir}"
      Dir.foreach(tmpdir) do |d|
        dir = File.join(tmpdir, d)
        if File.directory?(dir) 
          mv dir, src_dir unless ['.','..','.git'].include?d
        end  
      end
      rm_r tmpdir
    end
  end
  
  desc "Fetch the cvs repositories"
  task :fetch_cvs => :prepare do
    src_dir = CONFIGS['environment']['source_dir']
    CONFIGS['fetch']['cvs'].each do |cvs|
      puts "Fetching: #{cvs['desciption']}"
      cvs_repo = cvs['repository']
      module_path = cvs['module_path']
      item = cvs['item']
      
      tmpdir = generateTmpDirectory(src_dir)
      current_dir = File.expand_path('.')
      
      #we have to change directory. as a bug of cvs
      Dir.chdir(File.dirname(tmpdir))  
      sh "cvs -d #{cvs_repo} checkout -d #{File.join(File.basename(tmpdir), item)} #{File.join(module_path, item)}"
      
      Dir.foreach(tmpdir) do |d|
        dir = File.join(tmpdir, d)
        if File.directory?(dir) 
          mv dir, src_dir unless ['.','..','CVS'].include?d
        end  
      end
      
      Dir.chdir(current_dir)
      rm_r tmpdir
    end
  end
  
  
  # Generate a pathname to a temporary directory, which is inside the given 
  # directory
  #
  # * dir - the name of the given directory
  def generateTmpDirectory(dir)
    return File.join(dir, "%.6f" % Time.now.to_f)
  end

end