function stuff(birds, k)
max_bird = max(birds(:,5));
for i = 1:max_bird
    subplot(6,6,i)
    neighbours = k_nearest_neighbour(k, i,birds);
    title([num2str(k),'-nearest neighbour for i = ', num2str(i)]);
    xlabel('j = ');
    ylabel('Number of frames');
    xlim([1 max_bird+1]);
    counts = [histc(neighbours, 1:max_bird); 1:max_bird]';
    ranks = sortrows(counts, [-1, 2]);
    disp('bird i')
    disp('nearest neighbour:')
    ranks(1:2,:)
end

