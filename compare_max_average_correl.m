range = 70;
for i=1:33
    for j=i+1:33
        [c_range, c_max] = matching.test_correlation(get_bird(i+16), get_bird(j+16),-range, range);
        c_lcfm = lcfm_correlations(i,j);
        if c_max > c_lcfm
            fprintf(1, 'Birds (%d, %d)  max: %f  lcfm: %f\n',i,j,c_max, c_lcfm);
        end
    end
end

