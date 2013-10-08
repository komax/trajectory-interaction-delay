function [t1, t2] = compute_overlap(bird1, bird2)
frames = intersect(bird1(:,4), bird2(:,4));

t1 = nan(size(frames,1),5);
t2 = nan(size(frames,1),5);
for f = 1:size(frames,1)
    frame = frames(f);
    t1(f, 1:5) = bird1(bird1(:,4) == frame,:);
    t2(f, 1:5) = bird2(bird2(:,4) == frame,:);
end

