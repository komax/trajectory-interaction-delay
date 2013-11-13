function c = lcfm_correlations(i,j)
% given a matching, compute the directional correlation between the velocities at the matched points

[results, i, j, smoothed_i, smoothed_j] = get_saved_matching(i,j);

[velocities_i, frames_i] = smoothing.flight_velocities(smoothed_i);
[velocities_j, frames_j] = smoothing.flight_velocities(smoothed_j);

sum_dots = 0;
count = 0;
% missing first and last because we have to compute velocities
for row = 1:size(results,1)
    frame_i = results(row,4);
    frame_j = results(row,9);
    idx_i = find(frames_i == frame_i);
    idx_j = find(frames_j == frame_j);
    norm_v_i = matching.normalise_velocity(velocities_i(idx_i,:));
    norm_v_j = matching.normalise_velocity(velocities_j(idx_j,:));
    
    if ~isempty(norm_v_i) && ~isempty(norm_v_j)
        cosine = norm_v_i * norm_v_j';
        sum_dots = sum_dots + norm_v_i * norm_v_j';
        count = count + 1;
    else
        % there are some points left at the start and end with no match
        % i.e. anything matched with the first of either,
        % or anything matched with the last of either.
        % let's silently skip these.
        % TODO
    end

end
c = sum_dots / count;
