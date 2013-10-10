% interpolate: yes
% smoothing: yes
% overlap only: yes
% second data set: yes
order = 2;
cutoff = 0.1;
for i = 1:18
    bird_i = interpolate(get_bird(i + 16));
    for j = i+1:18
        bird_j = interpolate(get_bird(j + 16));
        [common_i, common_j] = matching.compute_overlap(bird_i, bird_j);
        smoothed_i = smoothing.apply_butterworth(common_i, order, cutoff);
        smoothed_j = smoothing.apply_butterworth(common_j, order, cutoff);

        m_ij = matching.discrete_lcfm(smoothed_i, smoothed_j);
        results = m_ij.getMatchedPoints();

        save(['precomputed/',date,'matching',num2str(i),'_', num2str(j)], 'results', 'i', 'j','smoothed_i', 'smoothed_j');
    end
end


