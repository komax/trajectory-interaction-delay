function g = build_correlation_graph(correlation, delay, cutoff_min, cutoff_max, varargin)
% default is false -- edge weights are distances/correlations, not delay
if nargin == 5
    show_delay = varargin{1};
else
    show_delay = false;
end

N = size(correlation,1);
cm = sparse(N,N);

for i = 1:N
    for j = i+1:N
        if correlation(i,j) >= cutoff_min && correlation(i,j) <= cutoff_max
            if delay(i,j) >= 0
                cm(i,j) = correlation(i,j);
                if show_delay
                    cm(i,j) = delay(i,j);
                end
            else
                cm(j,i) = correlation(i,j);
                if show_delay
                    cm(j,i) = delay(i,j);
                end
            end
        end
    end
end
g = biograph(cm);
set(g, 'ShowWeights', 'on');

if graphisdag(cm)
    fprintf(1,'Graph has no cycles\n');
else
    fprintf(1, 'Graph has at least one cycle\n');
end

end
