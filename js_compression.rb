#!/usr/bin/ruby

YUI_COMPRESSOR = "yuicompressor-2.4.2.jar"
PATTERN_JS = /\.js$/
PATTERN_CSS = /\.css$/

FILE_LIST = [
  "org.codefaces.web/src/org/codefaces/web/resources/json2.js",
  "org.codefaces.ui.themes/themes/azure/stylesheet/azure.css",
  "org.codefaces.web/public/javascripts/highlight/languages",
  "org.codefaces.web/public/javascripts/highlight/highlight.js",
  "org.codefaces.web/public/stylesheets/highlight",
  "org.codefaces.web/public/stylesheets",
]

def compress(in_file)
  if(File.directory?(in_file))
     Dir.foreach(in_file) do |f|
        file = File.join(in_file, f)
        compress_file(file) unless File.directory?file
     end
  else
    compress_file(in_file)
  end
end

def compress_file(in_file)
  index = in_file.index(PATTERN_JS) || in_file.index(PATTERN_CSS)
  out_file = in_file.clone.insert(index, '.min')
  %x[java -jar #{YUI_COMPRESSOR} #{in_file} -o #{out_file}]
end

FILE_LIST.each {|f| compress(f)}
