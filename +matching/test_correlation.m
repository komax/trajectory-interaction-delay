function [c_range, c_max] = test_correlation(bird_i, bird_j, tau_start, tau_end, varargin)
c_range = [];
for tau = tau_start:tau_end
    c_range(end+1,:) = [tau matching.dir_correlation(bird_i, bird_j, tau, varargin{2:end})];
end

hold on;
if nargin > 4
    colour = varargin{1};
else
    colour = 'b';
end
plot(c_range(:,1) ./ 35, c_range(:,2), colour)
xlabel('seconds')
c_max = max(c_range(:,2));
%%%max_tau = c_range(find(c_range(:,2) == c_max),:)
