function [results_c, results_delay] = compute_correlation_matrix(birds, range)
colour = 'b';
order = 2;
cutoff = 0.1;
num_birds = length(birds);

tau_start = -range;
tau_end = range;

results_c = repmat(NaN, num_birds, num_birds);
results_delay = repmat(NaN, num_birds, num_birds);

for i = birds
    bird_i = get_bird(i);
    for j = i+1:birds(end)
        fprintf(1, 'Comparing birds %d and %d\n',i,j);
        bird_j = get_bird(j);
        [c_range, c_max] = matching.test_correlation(bird_i,bird_j, tau_start, tau_end, colour); 
        % c_range is a N by 2 vector, where N is length(tau_start:tau_end), 
        % and the first column is the value for tau, the second is the 
        % correlation for that value of_tau
        % c_max is the largest value for c_tau.
        results_c(i,j) = c_max;
        delay = c_range((c_range(:,2) == c_max),1);
        if length(delay) > 1
            results_delay(i,j) = delay(round(end/2));
        else
            results_delay(i,j) = delay;
        end
    end
end




