function Y = apply_offset_filter(X,Hd)
%%% function Y = apply_offset_filter(X,Hd)
%%% Hd is the filter object
offset = X(1,1:3);

originX = X(:,1:3) - repmat(offset, length(X), 1);

originY = filter(Hd, originX);
Y(:,1:3) = originY + repmat(offset, length(X), 1);
Y(:,4:size(X,2)) = X(:,4:size(X,2)); 
