function d = estimate_frechet_distance(terrain);

lower_bound = max(terrain(end,1), terrain(1,end));

% double and search
upper_frontier = lower_bound;
while ~matching.depth_first_search(terrain, upper_frontier)
    if upper_frontier > 200
        d = Inf;
        return;
    end

    upper_frontier = upper_frontier * 2;
end

%%%fprintf('Frechet distance exists in interval: [%f, %f)\n',lower_bound, upper_frontier);
d = bsearch(lower_bound, upper_frontier, terrain);
end

function hi = bsearch(lo, hi, terrain)
    EPS = 0.05;
    count = 0;
    while (lo < hi - EPS);
        count = count + 1;
        mid = (lo + hi) / 2.0;
        if depth_first_search(terrain, mid)
            hi = mid;
        else
            lo = mid;
        end
    end
%%%    fprintf('%d iterations of bsearch\n',count);
end
