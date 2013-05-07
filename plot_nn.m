function stuff(birds)
for i = 1:16
    subplot(4,4,i)
    nearest_neighbour(i,birds);
    title(['Nearest neighbour for i = ', num2str(i)]);
    xlabel('j = ');
    ylabel('Number of frames');
    xlim([1 17]);
end

