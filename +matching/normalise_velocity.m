function dV = normalise_velocity(dVin)
%%% function dV = normalise_velocity(dVin)
%%% Convert all velocity measures to vectors of length 1
    dV = dVin ./ repmat(sqrt(sum(dVin .* dVin,2)), 1, 3);
end
