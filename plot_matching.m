function plot_matching(t1,t2, matching)
figure
hold on;
scatter3(t1(:,1),t1(:,2),t1(:,3),5,t1(:,4));
scatter3(t2(:,1),t2(:,2),t2(:,3),5,t2(:,4));

for n = 1:length(matching)
    i = matching(1,n);
    j = matching(2,n);
    plot3(t1(i,1),t1(i,2),t1(i,3),t2(i,1),t2(i,2),t2(i,3))
end

