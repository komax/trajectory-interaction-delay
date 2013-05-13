function results =  stuff(birds)
    results = nan(16,16)
    for i = 1:16
        bird_i = birds(birds(:,5) == i,:);
        for j = i:16
            bird_j = birds(birds(:,5) == j,:);
            results(i,j) = find_frechet(bird_i, bird_j);
        end
    end
end


