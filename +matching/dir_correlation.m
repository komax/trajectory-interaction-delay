function [C_tau] = directional_correlation(bird_i,bird_j,tau, varargin)
%%%function [C_tau] = directional_correlation(bird_i, bird_j, tau)
%%%function [C_tau] = directional_correlation(bird_i, bird_j, tau, 'nofilter')
%%%function [C_tau] = directional_correlation(bird_i, bird_j, tau, order, cutoff)
%%% if no 'order' and 'cutoff' values are specified, default of order = 2 
%%% normalised cutoff frequency = 0.1 will be used
%%%
%%% C_ij(tau) = < v_i(t) * v_j(t+tau) >
%%% C_tau will range from [-1.0, to 1.0]

[dX, frame_ids_i] = smoothing.flight_velocities(bird_i);
[dY, frame_ids_j] = smoothing.flight_velocities(bird_j);

% smoothing parameters
if nargin == 3
    order = 2;
    cutoff = 0.1;
    dX = smoothing.apply_butterworth(dX, order, cutoff);
    dY = smoothing.apply_butterworth(dY, order, cutoff);
elseif nargin == 4 && strcmp(varargin{1},'nofilter')
    % nofilter, leave dX and dY unchanged
else
    order = varargin{1};
    cutoff = varargin{2};
    dX = smoothing.apply_butterworth(dX, order, cutoff);
    dY = smoothing.apply_butterworth(dY, order, cutoff);
end


frames = union(frame_ids_i,frame_ids_j); 

norm_v_i = normalise_velocity(dX);
norm_v_j = normalise_velocity(dY);

sum_dots = 0;
count = 0;
for f = frames'
    idx_i = find(frame_ids_i == f);
    idx_j = find(frame_ids_j == (f - tau));
    if length(idx_i) > 1 || length(idx_j) > 1
        fprintf(2, 'error, multiple points match???\n');
    end
    if ~isempty(idx_j) && ~isempty(idx_i)
        sum_dots = sum_dots + norm_v_i(idx_i,:) * norm_v_j(idx_j,:)';
        count = count + 1;
    end
end

%%%C_tau = sum_dots / count;
C_tau = sum_dots / length(frames);
end

function dV = normalise_velocity(dVin)
%%% function dV = normalise_velocity(dVin)
%%% Convert all velocity measures to vectors of length 1
    dV = dVin ./ repmat(sqrt(sum(dVin .* dVin,2)), 1, 3);
end
