function nearest_neighbour(k, i, bird_matrix)

bird_i = bird_matrix(bird_matrix(:,5) == i, :);
start_frame = min(bird_i(:,4));
stop_frame = max(bird_i(:,4));
neighbour = [];
for f = start_frame:stop_frame
   b_if = bird_i(bird_i(:,4) == f,:);
   if b_if
       same_times = bird_matrix(bird_matrix(:,4) == f, :);
       same_times = same_times(same_times(:,5) ~= i,:);
       if same_times
           num_points = size(same_times,1); % num of rows
           dupes = repmat(b_if, num_points,1);
           distances = sqrt(sum((same_times - dupes).^2,2));
           idx = find(distances == min(distances));
           neighbour(end+1) = same_times(idx,5);
        end
    end
end
N = histc(neighbour, 1:16);
bar(1:16,N, 'histc');



end
