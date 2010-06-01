
module Helper
  
  # Generate a pathname to a temporary directory, which is inside the given 
  # directory
  #
  # * dir - the name of the given directory
  def generate_tmp_directory(dir)
    return File.join(dir, "%.6f" % Time.now.to_f)
  end

  # Check and Create a new directory
  # raise an exception if the directory is already existed and not
  # empty
  # 
  # * dir_path - the path of the directory to be created
  def create_directory(dir_path)
    if(File.exists?(dir_path) && !is_directory_empty?(dir_path))
      raise "#{dir_path} is not empty."
    else
      mkdir_p dir_path unless File.exists?(dir_path)
    end

  end

  # return true if the dir_path exists but empty
  def is_directory_empty?(dir_path)
    Dir.entries(dir_path).select{|x| x unless ['.','..'].include?x}.empty?
  end


  def checkout(output_dir, configs)
    puts configs['name']
    scm = configs['scm']
    repo = configs['repository']
    name = configs['name']
    unpack = configs['unpack']
    tmp_dir = generate_tmp_directory(output_dir)

    case scm
      when "file"
        sh "mkdir -p #{tmp_dir}"  
        sh "cp #{repo} #{tmp_dir}/#{name}"
      when "git"
        sh "git clone #{repo} #{tmp_dir}; rm -Rf #{tmp_dir}/.git"
      when "http"
        sh "mkdir -p #{tmp_dir}"  
        sh "wget #{repo} -O#{tmp_dir}/#{name}"
      when "cvs"
        name = configs['name']
        module_path = configs['module_path']
        current_dir = File.expand_path('.')
        sh "mkdir -p #{tmp_dir}"
        Dir.chdir(File.expand_path(tmp_dir))
        sh "cvs -d #{repo} checkout -d #{name} #{module_path}"
        sh "rm -Rf CVS"
        Dir.chdir(current_dir)
    end

    if unpack == true
      case name
        when /\.zip$/
          sh "unzip #{tmp_dir}/#{name} -d#{tmp_dir}/#{File.basename(name,
              ".zip")}"
          sh "rm -Rf #{tmp_dir}/#{name}"
        when /\.tar\.gz$/
          extract_dir = File.join(tmp_dir, File.basename(name,".tar.gz"))
          sh "mkdir -p #{extract_dir}"
          sh "tar -xvf #{tmp_dir}/#{name} -C #{extract_dir}" 
          sh "rm -Rf #{tmp_dir}/#{name}"          
        else
          raise "Cannot unpack #{tmp_dir}/#{name}"
      end
    end

    sh "mv #{tmp_dir}/* #{output_dir}/"
    rm_r tmp_dir
  end
end
