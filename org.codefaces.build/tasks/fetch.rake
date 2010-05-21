
namespace :fetch do
  
  task :default => [:fetch_http_servletbridge, :fetch_servletbridge, :fetch_codefaces]
  
  desc "Fetch org.eclipse.equinox.http.servletbridge from the CVS repository"
  task :fetch_http_servletbridge => [:fetch_codefaces] do
  cvsRoot = ":pserver:anonymous@dev.eclipse.org:/cvsroot/rt"
    bundle = "org.eclipse.equinox.http.servletbridge"
    moduleName = "org.eclipse.equinox/server-side/bundles"
  
    sh "cvs -d #{cvsRoot} checkout -d #{File.join(SRC_DIR, bundle)} #{File.join(moduleName, bundle)}"
  end

  desc "Fetch org.eclipse.equinox.servletbridge from the CVS repository"
  task :fetch_servletbridge => [:fetch_codefaces] do
    cvsRoot = ":pserver:anonymous@dev.eclipse.org:/cvsroot/rt"
    bundle = "org.eclipse.equinox.servletbridge"
    moduleName = "org.eclipse.equinox/server-side/bundles"
  
    sh "cvs -d #{cvsRoot} checkout -d #{File.join(SRC_DIR, bundle)} #{File.join(moduleName, bundle)}"
  end

  desc "Fetch the CodeFaces repository"
  task :fetch_codefaces do
    sh "git clone #{GIT_CODEFACES_REPO} #{SRC_DIR}"
  end
end