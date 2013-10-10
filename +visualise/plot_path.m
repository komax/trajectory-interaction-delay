function plot_path(t1, t2, matching_java_obj, name1, name2)
clf;
xlabel(name2)
ylabel(name1)
hold on;

idx = [matching_java_obj.i matching_java_obj.j];
idx = [ 1 1; 2 2; 2 3; 3 4; 4 5; 5 5; 6 5;];

%%%results = matching_java_obj.getMatchedPoints();
x = [];
y = [];
for row = 1:size(idx,1)
%%%    p = results(row,1:3);
%%%    frame_1 = results(row,4);
%%%    bird_id1 = results(row,5);
%%%    q = results(row,6:8);
%%%    frame_2 = results(row,9);
%%%    bird_id2 = results(row,10);
%%%    x(row) = frame_1 + 1;
%%%    y(row) = frame_2 + 1;
%%%    fprintf(1,'(%d, %d)\n',frame_1, frame_2);
    x(row) = idx(row, 2) + 1; % how far along bird j we are
    y(row) = size(t1,1) - idx(row, 1); % how far along bird i we are
end
show_equal_times(t1,t2,matching_java_obj);
plot(x, y,'r');


end

function show_equal_times(t1, t2, matching_java_obj)
    
    start_frame = min([t1(:,4); t2(:,4)]);
    stop_frame = max([t1(:,4); t2(:,4)]);
    
    y = [];
    for col = 1:size(t2,1)
        time_est = col + start_frame;
        position = time_est - start_frame;
        if position
            y(col) = size(t1,1) - position;
        else
            y(col) = NaN;
        end

    end
    plot(y,'k')
%%%    for frame = start_frame:stop_frame
%%%        index_t1 = find(results(results(:,4) == frame)); 
%%%        index_t2 = find(results(results(:,9) == frame));
%%%        if size(index_t1,1) > 0 && size(index_t2,1) > 0
%%%            for i = index_t1'
%%%                
%%%                for j = index_t2'
%%%                    fprintf(1,'(%d, %d)\n',i,j)
%%%                    x(row) = j + 1;
%%%                    y(row) = size(t1,1) - i;
%%%                    row = row + 1;
%%%                end
%%%            end
%%%        end

%%%    end
%%%    plot(x, y,'w')
end
