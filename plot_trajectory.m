function plot_trajectory(t, varargin)
%%%function plot_trajectory(t) or plot_trajectory(t, colourstring)

if nargin == 1
    colour = 'b';
else
    colour = varargin{1};
end

plot3(t(:,1),t(:,2), t(:,3), ['.-', colour])
