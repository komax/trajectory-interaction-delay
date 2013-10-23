function plot_trajectory(t, varargin)
%%%function plot_trajectory(t) or plot_trajectory(t, colourstring)


plot3(t(:,1),t(:,2), t(:,3),  varargin{:})
