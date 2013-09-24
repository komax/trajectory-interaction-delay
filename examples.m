%%%example_smoothing;

% given two trajectories, compute the distance terrain
i = 1; j = 2;
bird_i = smoothing.apply_butterworth(get_bird(i), 2, 0.1);
bird_j = smoothing.apply_butterworth(get_bird(j), 2, 0.1);
%%%figure; hold on;
%%%plot_trajectory(bird_i,'b');
%%%plot_trajectory(bird_j,'r');
%%%terrain = matching.compute_distance_terrain(bird_i, bird_j);
%%%figure; imagesc(terrain); axis equal;

% given the distance terrain estimate the Frechet distance 
% (using binary search and DfS)
%%%d_ij = matching.estimate_frechet_distance(terrain);
%%%fprintf(1,'Estimated Frechet distance for birds %d and %d: %f\n', i, j, d_ij);

% compute a locally correct Frechet matching
matching_ij = matching.discrete_lcfm(bird_i, bird_j);
%%%fprintf(2,'Warning: the discrete LCF matching algorithm is currently a little buggy\n');



% visualise a locally correct Frechet matching
figure;
visualise.plot_matching(bird_i, bird_j, matching_ij);
figure;
visualise.plot_path(bird_i, bird_j, matching_ij);

