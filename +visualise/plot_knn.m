function stuff(birds, k)
for i = 1:16
    subplot(4,4,i)
    k_nearest_neighbour(k, i,birds);
    title([num2str(k),'-nearest neighbour for i = ', num2str(i)]);
    xlabel('j = ');
    ylabel('Number of frames');
    xlim([1 17]);
end

