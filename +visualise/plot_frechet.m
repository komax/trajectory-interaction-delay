function results =  stuff(birds, num_birds)
    results = nan(num_birds, num_birds);
    for i = 1:num_birds
        bird_i = birds(birds(:,5) == i,:);
        for j = i+1:num_birds
            bird_j = birds(birds(:,5) == j,:);
            [t1, t2] = matching.compute_overlap(bird_i, bird_j);
            fprintf(1,'Estimating Frechet distance for birds %d and %d\n',i,j);
            terrain = matching.compute_distance_terrain(t1, t2);
            results(i,j) = matching.estimate_frechet_distance(terrain);

            save frechet_distances.mat results;
        end
    end
end


