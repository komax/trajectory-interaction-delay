function [start_long, end_long, mean_dist, mean_delay] = get_similar(bird_id1, bird_id2, cutoff)
%%%function [start_long, end_long, mean_dist, mean_delay] = get_similar(bird_id1, bird_id2, cutoff)

[results, i, j, data_i, data_j] = get_saved_matching(bird_id1, bird_id2);
longest_run = [0 0];
start_pair = -1;
end_pair = -1;
distances = [];
delays = [];
for row = 1:size(results,1)
    t1 = results(row,4);
    t2 = results(row,9);
    p = results(row,1:3);
    q = results(row,6:8);
    d = norm(p - q, 2);
    delta_t = t1 - t2;
    distances(row) = d;
    delays(row) = delta_t / 35;

    if d <= cutoff && start_pair > 0
        end_pair = row;
    elseif d <= cutoff
        start_pair = row;
        end_pair = row;
    else
        if start_pair > 0
            if end_pair - start_pair >= longest_run(2) - longest_run(1)
                longest_run = [start_pair end_pair];
            end
%%%            fprintf(1, 'Run less than %f from %d to %d\n', cutoff, start_pair, end_pair);
%%%            mean_dist = mean(distances(start_pair:end_pair));
%%%            mean_delay = mean(delays(start_pair:end_pair));
%%%            fprintf('Mean distance: %f metres, Mean delay: %f seconds\n',mean_dist, mean_delay);
        end
        start_pair = -1;
    end
end

start_long = longest_run(1);
end_long = longest_run(2);
if start_long == 0
    mean_dist = NaN;
    mean_delay = NaN;
else
    mean_dist = mean(distances(start_long:end_long));
    mean_delay = mean(delays(start_long:end_long));
end

end
