require 'tasks/helper'

namespace :fetch do
  include Helper

  # Fetch the repository and tools
  task :default => [:fetch_repo, :fetch_build_tools]

  # Parpare the directory for fetching
  task :prepare_src_dir do
    src_dir = File.expand_path(CONFIGS['environment']['source_dir'])
    create_directory(src_dir)
  end
  
  # Fetch the repositories
  desc "Fetch the Codefaces repository"
  task :fetch_repo => :prepare_src_dir do
    src_dir = CONFIGS['environment']['source_dir']
    CONFIGS['fetch'].each do |config|
      puts "Fetching: #{config['description']}"
      checkout(src_dir, config)
    end
  end

  # Parpare the tool directory for fetching
  task :prepare_tool_dir do
    tool_dir = File.expand_path(CONFIGS['environment']['tool_dir']) 
    create_directory(tool_dir)
  end

  # fetch the tools needed
  desc "Fetch the build tools and required bundles"
  task :fetch_build_tools => 
       [:fetch_eclipse_platform, :fetch_target_platform, :fetch_servlet_bridge]


  # fetch the eclipse platform
  task :fetch_eclipse_platform => :prepare_tool_dir do
    tool_dir = CONFIGS['environment']['tool_dir']
    platform = CONFIGS['tools']['eclipse_platform']
    puts "Fetching: #{platform['description']}"
    checkout(tool_dir, platform)
  end

  # fetch the target platform
  task :fetch_target_platform => :prepare_tool_dir do
    tool_dir = CONFIGS['environment']['tool_dir']
    target_platform = CONFIGS['tools']['target_platform']
    puts "Fetching: #{target_platform['description']}"
    checkout(tool_dir, target_platform)
  end

  # fetch the servlet bridges
  task :fetch_servlet_bridge => :prepare_tool_dir do
    tool_dir = CONFIGS['environment']['tool_dir']
    bridges = CONFIGS['tools']['servlet_bridge']
    bridges.each do |b|
      puts "Fetching: #{b['description']}"
      checkout(tool_dir, b)
    end
  end
end
