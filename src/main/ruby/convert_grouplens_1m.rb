File.open(ARGV[0]).each do |line|
  print line.split("::").join(",")
end
