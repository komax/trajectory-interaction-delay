function plot_path(name1, name2, varargin)
if nargin == 3
    colour = varargin{1};
else
    colour = 'b';
end
xlabel(num2str(name1))
ylabel(num2str(name2))
hold on;
axis square;

load(['precomputed/matching',num2str(name1),'_',num2str(name2),'.mat']);
% loads results, i, j, smoothed_i, smoothed_j

x = [];
y = [];

for n = 1:size(results,1)
    x(n) = results(n,4);
    y(n) = results(n,9);
end
plot(x,y, colour)
start_frame = min([results(:,4); results(:,9)]);
stop_frame = max([results(:,4); results(:,9)]);
plot(start_frame:stop_frame,start_frame:stop_frame,'k')

figure
terrain = matching.compute_distance_terrain(smoothed_i, smoothed_j);
imagesc(terrain);
axis square;
hold on;
plot(y - start_frame, stop_frame - x - 1,'w')

