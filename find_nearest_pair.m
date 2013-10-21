function idx = find_nearest_pair(res, i, max_bird)
    lessthan_i = res(1:i-1,i);
    morethan_i = res(i,i+1:max_bird);
    other_birds = [lessthan_i' morethan_i];
    top = min(other_birds);
    idx_col = find(morethan_i == top);
    idx_row = find(lessthan_i == top);
    if idx_col
        idx = idx_col + i;
    elseif idx_row
        idx = idx_row;
    else
        fprintf(2,'error finding closest bird via Frechet distance\n');
        idx = -1;
    end

end



