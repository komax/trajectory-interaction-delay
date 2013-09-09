function [frame_ids, z, dz, ddz] = blah(i,varargin);
if nargin == 1
    dim = 3; % z axis
elseif nargin == 2
    dim = varargin{1};
end
c = 'brg';
bird_i = get_bird(i);
frame_ids = bird_i(:,4);
z = bird_i(:,dim);
dz = z(2:end,:) - z(1:end-1,:);
ddz = dz(2:end,:) - dz(1:end-1,:);
figure(1)
hold on
plot(frame_ids,z,c(dim))
title(['Bird ',int2str(i),'displacement']);
figure(2)
hold on
plot(frame_ids(2:end),dz, c(dim))
title(['Bird ',int2str(i),'velocity']);
figure(3)
hold on
plot(frame_ids(3:end),ddz, c(dim))
title(['Bird ',int2str(i),'acceleration']);
