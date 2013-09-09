

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


