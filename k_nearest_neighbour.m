function neighbour = k_nearest_neighbour(k, i, bird_matrix)
%%%function nearest_neighbour(k, i, bird_matrix)
%%%plot a histogram
max_birds = max(bird_matrix(:,5));
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
           idx = 1:num_points;
           distances = [ idx' sqrt(sum((same_times - dupes).^2,2)) ];
           closest = sortrows(distances,[2]);
           for n = 1:k
               if n < length(closest)
                   neighbour(end+1) = same_times(closest(n,1),5);
               end
           end

        end
    end
end
N = histc(neighbour, 1:max_birds);
bar(1:max_birds,N, 'histc');



end
