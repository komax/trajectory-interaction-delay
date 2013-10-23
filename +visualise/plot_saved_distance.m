function plot_saved_distance(bird_id1, bird_id2)

results = get_saved_matching(bird_id1, bird_id2);

figure;
subplot(2,1,1);
ylabel('metres')
hold on;
y = [];
delays = [];
for row = 1:size(results,1)
    t1 = results(row,4);
    t2 = results(row,9);
    p = results(row,1:3);
    q = results(row,6:8);
    d = norm(p - q, 2);
    delta_t = t1 - t2;
    if delta_t == 0
        fprintf(2,'Matched pairs are from the same frame.\n');
        scatter(row,d,'k','filled')
    elseif delta_t > 0
        scatter(row, d, 'b','filled')
    else
        scatter(row, d, 'r', 'filled')
    end
    y(row) = d;
    delays(row) = delta_t;
end

subplot(2,1,2);
plot(delays ./ 35)
ylabel('seconds')
