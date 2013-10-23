function list_similar(max_bird, cutoff, interest)
count = 0
for i = 1:max_bird
    for j = i+1:max_bird
        [s, t, mean_dist, mean_delay] = process.get_similar(i,j, cutoff);
        len = t - s;
        if s ~= 0 && len > interest
            fprintf('Birds (%d, %d) Run of %d pairs, mean delay %f seconds, distance: %f metres\n',i,j,len, mean_delay, mean_dist);
            count = count + 1;
        end
    end
end

count

