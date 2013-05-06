function stuff(birds)
for i = 1:16
    subplot(4,4,i)
    title(['Nearest neighbour for i = ', num2str(i)]);
    xlabel('j = ');
    nearest_neighbour(i,birds);
end

