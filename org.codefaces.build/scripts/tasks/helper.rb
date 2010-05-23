
module Helper
  
  # Generate a pathname to a temporary directory, which is inside the given 
  # directory
  #
  # * dir - the name of the given directory
  def generateTmpDirectory(dir)
    return File.join(dir, "%.6f" % Time.now.to_f)
  end
  
end