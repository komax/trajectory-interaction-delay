

%% given a trajectory, apply a Butterworth to smooth out changes in position
trajectory = get_bird(5); 
% 5 is the trajectory with bird_id 5 in the first data set. 
% 21 (e.g. 5 + 16) is the trajectory with bird_id 5 in the second set.

order = 2;
normalised_cutoff = 0.1; % should be between 0 and 1
smoothed_trajectory = smoothing.apply_butterworth(trajectory, order, normalised_cutoff);

% compare smoothed (red) and original (blue) trajectories
figure; hold on;
title('Comparison of original and smoothed trajectory (bird 5)');
plot_trajectory(trajectory);
plot_trajectory(smoothed_trajectory,'r');

% given two trajectories, compute the distance terrain
bird_i = smoothing.apply_butterworth(get_bird(11), 2, 0.1);
bird_j = smoothing.apply_butterworth(get_bird(15), 2, 0.1);
figure; hold on;
plot_trajectory(bird_i,'b');
plot_trajectory(bird_j,'r');

terrain = matching.compute_distance_terrain(bird_i, bird_j);
figure; imagesc(terrain);

% given the distance terrain estimate the Frechet distance 
% (using binary search and DfS)
d_ij = matching.estimate_frechet_distance(terrain);
disp(['Estimated Frechet distance for birds 11 and 15: ', num2str(d_ij)]);

% compute a locally correct Frechet matching
matching_ij = matching.discrete_lcfm(bird_i, bird_j);


% visualise a locally correct Frechet matching
figure;
visualise.plot_matching(bird_i, bird_j, matching_ij);
figure;
visualise.plot_path(bird_i, bird_j, matching_ij);

