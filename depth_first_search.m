function [path_exists] = dfs(terrain_matrix, cutoff)
%%% function [path] = dfs(terrain_matrix)
    height = size(terrain_matrix,1);
    width = size(terrain_matrix,2);

    path_exists = false;
    i = height;
    j = 1;
    fringe(1).i = i;
    fringe(1).j = j;
  
    expanded = sparse(height, width);
    expanded(i,j) = 1;
    while length(fringe) 
        % pop
        i = fringe(end).i;
        j = fringe(end).j;
        fringe = fringe(:,1:end-1);


        % if we have reached our goal
        if (i == 1) && (j == width)
            path_exists = true;
            return;
        end

        for i_progress = [0 -1]; % since the terrain is "upside down"
            for j_progress = [0 1];
                curr_i = i + i_progress;
                curr_j = j + j_progress;
                if (i_progress == 0) && (j_progress == 0) 
                    continue;

                % if we leave the boundary
                elseif (curr_i < 1) || (curr_j > width)
                    continue;

                %if the distance is too large
                elseif terrain_matrix(curr_i, curr_j) == Inf
                    % find an approximate distance somehow
                    % maybe continue, maybe not
                    if rand > 0.5
                        continue;
                    end
%%%                    fprintf('oh no what to do?\n');
                elseif terrain_matrix(curr_i, curr_j) > cutoff
                    continue;
                end % end if
                
                if ~expanded(curr_i,curr_j)
                    top_of_stack = length(fringe);
                    fringe(top_of_stack + 1).i = curr_i;
                    fringe(top_of_stack + 1).j = curr_j;
                    expanded(curr_i,curr_j) = 1;
                end
                    
                                
            end % end for j
        end % end for i

    end

end

