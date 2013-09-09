function plot_signal(bird_id, varargin)
bird = get_bird(bird_id);
x = bird(:,1);
y = bird(:,2);
z = bird(:,3);
frame_id = bird(:,4);

%%%figure
title(['Bird ', int2str(bird_id)]);
hold on
if nargin > 1
    disp(varargin)
    dim = varargin{1};
    data = bird(:,dim);
    plot(frame_id, data, 'k');
    
else
    plot(frame_id, x,'b');
    plot(frame_id, y,'r');
    plot(frame_id, z,'g');
end


