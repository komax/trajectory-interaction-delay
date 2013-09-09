function data = get_bird_by_time(bird_id)
bird_i = get_bird(bird_id);
frame_id = bird_i(:,4);
min_frame = min(frame_id);
max_frame = max(frame_id);
frames = min_frame:1:max_frame;
data = nan(length(frames),4);
data(:,4) = frames;
for f = frames
    row = bird_i(bird_i(:,4) == f,:);
%%%    data(f - min_frame + 1, :)
%%%    f - min_frame + 1
%%%    disp('row');
%%%    disp(row)
%%%    row(:,1:3)
    if size(row,1) > 0
        data(f - min_frame + 1, :) = row(:,1:4); % matlab index starts from 1
    end

end

