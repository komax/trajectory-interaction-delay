function [distances, delays] = lcfm_matrix()

N = 33;
delays = sparse(N,N);
distances = sparse(N,N);
for i=1:N
    for j=i+1:N
        [distance, delay] = visualise.plot_saved_matching(i,j, 'noplot');
        delays(i,j) = mean(delay);
        distances(i,j) = max(distance);
    end
end

figure; imagesc(delays)
figure; imagesc(distances)


end

