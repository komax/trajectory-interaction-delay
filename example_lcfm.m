function example_lcfm(bird_id1, bird_id2)


bird_i = smoothing.apply_butterworth(get_bird(bird_id1), 2, 0.1);
bird_j = smoothing.apply_butterworth(get_bird(bird_id2), 2, 0.1);

matching_ij = matching.discrete_lcfm(bird_i, bird_j);

% visualise a locally correct Frechet matching
figure; visualise.plot_matching(bird_i, bird_j, matching_ij);

figure; 
visualise.plot_path(bird_i, bird_j, matching_ij, ['Bird ', num2str(bird_id1)], ['Bird ',num2str(bird_id2)]);

