function [path_exists] = dfs(terrain_matrix, cutoff)
%%% starting at the bottom left corner of the terrain_matrix (i.e. (1,end)),
%%% find a path to the top right corner (end, 1), where each point on the
%%% path is less than cutoff.
 
    height = size(terrain_matrix,1);
    width = size(terrain_matrix,2);

    path_exists = false;
    i = height;
    j = 1;
    fringe = []; % empty array, to be filled with structs with (i,j) pairs
    fringe(1).i = i;
    fringe(1).j = j;
  
    % matrix to store which nodes have been expanded
    expanded = sparse(height, width); 
    expanded(i,j) = 1; 
    while length(fringe) 
        [i,j, fringe] = pop(fringe);
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
                elseif (curr_i < 1) || (curr_j > width)
                    continue;
                elseif (terrain_matrix(curr_i, curr_j) ~= Inf) && terrain_matrix(curr_i, curr_j) > cutoff
                    continue;
                end % end if
                
                if ~expanded(curr_i,curr_j)
                    fringe = push(fringe, curr_i, curr_j);
                    expanded(curr_i,curr_j) = 1;
                end
                                
            end % end for j
        end % end for i

    end
    % note: if we reach here then path_exists = false
end

function [stack] = push(stack, data_i, data_j)
    top = length(stack);
    stack(top + 1).i = data_i;
    stack(top + 1).j = data_j;
end

function [idx1, idx2, stack] = pop(stack)
    idx1 = stack(end).i;
    idx2 = stack(end).j;
    stack = stack(:,1:end-1);
end
