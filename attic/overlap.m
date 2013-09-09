function o = overlap()
for i=1:16
    i_frames = get_bird(i);
    i_frames = i_frames(:,4);
    for j=1:16
        j_frames = get_bird(j);
        j_frames = j_frames(:,4);
        o(i,j) = length(intersect(i_frames,j_frames));
    end
end
imagesc(o);
