function result = compare(correlation, distances, correlation_min, distances_max)

result = zeros(size(correlation));
edge_in_both = 0;
no_edge_in_both = 0;
edge_only_in_lcfm = 0;
edge_only_in_correl = 0;
for i = 1:size(correlation,1)
    for j = i+1:size(correlation,2)
        if distances(i,j) <= distances_max && correlation(i,j) >= correlation_min
            % edge in both
            result(i,j) = 1;
            edge_in_both = edge_in_both + 1;
        else
            if distances(i,j) > distances_max && correlation(i,j) < correlation_min
                % no edge in either
                result(i,j) = 0;
                no_edge_in_both = no_edge_in_both + 1;
            else
                if distances(i,j) > distances_max
                    % edge in correlation not in lcfm
                    edge_only_in_correl = edge_only_in_correl + 1;
                    result(i,j) = 3;
                elseif correlation(i,j) < correlation_min
                    result(i,j) = 2;
                    edge_only_in_lcfm = edge_only_in_lcfm + 1;
                else 
                    fprintf(2,'error\n');
                    result(i,j) = 10;
                end
            end
        end
    end
end

fprintf(1, '     %d     %d\n', edge_in_both, edge_only_in_lcfm);
fprintf(1, '     %d     %d\n', edge_only_in_correl, no_edge_in_both);
