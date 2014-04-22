function plot_all_birds()
%%%colors = 'bgrcmykw'
for i = 1:16
%%%for i = [11 12 15]
    m = get_bird(i);
%%%    s = colors(mod(i,8) + 1);
    hold on
    scatter3(m(:,1), m(:,2), m(:,3),5,m(:,4))
end

end

