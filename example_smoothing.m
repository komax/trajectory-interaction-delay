function example_smoothing(bird_id, varargin)
%%%function example_smoothing(bird_id)
% given a trajectory, apply a Butterworth filter to smooth out changes in position
% 5 is the trajectory with bird_id 5 in the first data set. 
% 21 (e.g. 5 + 16) is the trajectory with bird_id 5 in the second set.
    trajectory = get_bird(bird_id); 


    order = 2;
    if nargin > 1
        normalised_cutoff = varargin{1}
    else
        normalised_cutoff = 0.1; % should be between 0 and 1
    end
    smoothed_trajectory = smoothing.apply_butterworth(trajectory, order, normalised_cutoff);

    % compare smoothed (red) and original (blue) trajectories
    figure; hold on;
    title(['Comparison of original and smoothed trajectory (bird ', num2str(bird_id), ')']);
    plot_trajectory(trajectory);
    plot_trajectory(smoothed_trajectory,'r');

end
