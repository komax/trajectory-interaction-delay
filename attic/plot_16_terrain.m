function stuff( i)
clf
bird_i = get_bird(i);
for j= 1:16
    if i ~= j
        subplot(4,4,j)
        terrain = compute_distance_terrain(bird_i, get_bird(j));
        imagesc(terrain);
        axis square;
        title(['Distance terrain of birds (', num2str(i), ',', num2str(j),')']);
    end
end

end

