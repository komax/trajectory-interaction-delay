function gen_corr_figs(i, max_bird)
    colours = 'bgrcmyb';
    bird_i = get_bird(i);
    h = figure; hold on;
    title(['Direction correlation bird ', num2str(i)]);
    for j = 1:max_bird
        if i ~= j
            bird_j = get_bird(j);
            [c_range, c_max] = matching.test_correlation(bird_i, bird_j,-35,35, colours(mod(j,7) + 1));
            if c_max > 0.4
                fprintf(1, 'Bird %d matches %d with correlation %f\n',i,j,c_max)
            end
        end
    end
    saveas(h, ['figures/dir_corr_set2_',num2str(i), '.png'],'png');
end
    



