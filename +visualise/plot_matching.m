function [distances, delay] = plot_matching(t1,t2,matching_java_obj)
% 3D plot for the matching between the trajectories t1 and t2
clf;
hold on;
matching = javamatching2matlab(matching_java_obj);
plot3(t1(:,1),t1(:,2),t1(:,3),'k');
plot3(t2(:,1),t2(:,2),t2(:,3),'b');
distances = [];
delay = [];
for n = 1:length(matching)
    i = matching(n,1) + 1;
    j = matching(n,2) + 1;
    plot3([t1(i,1); t2(j,1)],[t1(i,2); t2(j,2)],[t1(i,3); t2(j,3)],'r-')
    p1 = t1(i,1:3);
    p2 = t2(j,1:3);
    d = norm(p1 - p2, 2);
    if size(t1, 2) >= 4 && size(t2, 2) >= 4
        delay(n) = t1(i,4) - t2(j,4);
    end

    distances(n) = d;
end
if size(t1,2) >= 4 && size(t2,2) >=4
    scatter3(t1(:,1),t1(:,2),t1(:,3),30,t1(:,4),'filled');
    scatter3(t2(:,1),t2(:,2),t2(:,3),30,t2(:,4),'filled');
end

grid on;
axis equal;
